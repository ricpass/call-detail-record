package com.ricardopassarella.calldetails.domain;

import com.ricardopassarella.calldetails.domain.exception.FailedReadUploadCallDetails;
import com.ricardopassarella.calldetails.domain.exception.FailedToParseUploadCallDetails;
import com.ricardopassarella.calldetails.domain.exception.UnknownCurrencyException;
import com.ricardopassarella.calldetails.dto.CallDetailsUploadRow;
import com.ricardopassarella.calldetails.dto.CallFinanceInsert;
import com.ricardopassarella.calldetails.dto.CallLog;
import com.ricardopassarella.calldetails.dto.ExchangeRate;
import com.univocity.parsers.common.TextParsingException;
import com.univocity.parsers.csv.CsvRoutines;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class CallDetailsImportService {

    private final CsvRoutines csvRoutines;
    private final CallDetailsRepository repository;

    @Value("${call.details.import.batch-size:10000}")
    private int BATCH_SIZE;

    @Transactional
    public void parseAndInsertCallDetails(MultipartFile multipartFile) {
        try {
            InputStream input = multipartFile.getInputStream();

            List<CallLog> callLogs = new ArrayList<>();
            List<CallFinanceInsert> callFinances = new ArrayList<>();

            for (CallDetailsUploadRow row : csvRoutines.iterate(CallDetailsUploadRow.class, input, StandardCharsets.UTF_8)) {
                String callLogId = UUID.randomUUID()
                                       .toString();
                CallLog callLog = createCallLogInsert(row, callLogId);
                CallFinanceInsert callFinance = createCallFinanceInsert(row, callLogId);

                callLogs.add(callLog);
                callFinances.add(callFinance);

                if (callLogs.size() == BATCH_SIZE) {
                    repository.insertBatch(callLogs,
                                           callFinances);
                    callLogs = new ArrayList<>();
                    callFinances = new ArrayList<>();
                }
            }

            if (callLogs.size() > 0) {
                repository.insertBatch(callLogs,
                                       callFinances);
            }

        } catch (IOException e) {
            throw new FailedReadUploadCallDetails(e);
        } catch (TextParsingException e) {
            throw new FailedToParseUploadCallDetails(e.getLineIndex(), e);
        }
    }

    private CallLog createCallLogInsert(CallDetailsUploadRow row, String callLogId) {
        LocalDateTime callEnd = LocalDateTime.of(row.getCallDate(), row.getEndTime());
        LocalDateTime callStart = callEnd.minusSeconds(row.getDuration());

        return CallLog.builder()
                      .uuid(callLogId)
                      .callerId(row.getCallerId())
                      .recipient(row.getRecipient())
                      .callStart(callStart)
                      .callEnd(callEnd)
                      .reference(row.getReference())
                      .build();
    }

    private CallFinanceInsert createCallFinanceInsert(CallDetailsUploadRow row, String callLogId) {
        // TODO cache exchangeRate
        Optional<ExchangeRate> exchangeRate = repository.getExchangeRate(row.getCurrency());
        if (exchangeRate.isEmpty()) {
            throw new UnknownCurrencyException(row.getCurrency(), row.getReference());
        }

        ExchangeRate rate = exchangeRate.get();

        BigDecimal cost = row.getLocalCost()
                             .multiply(rate.getExchangeRate())
                             .setScale(3, RoundingMode.HALF_UP);

        return CallFinanceInsert.builder()
                                .localCost(row.getLocalCost())
                                .localCurrency(row.getCurrency())
                                .cost(cost)
                                .exchangeRate(rate.getExchangeRate())
                                .callLogId(callLogId)
                                .build();
    }

}
