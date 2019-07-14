package com.ricardopassarella.caller.domain;

import com.ricardopassarella.calldetails.domain.CallDetailsFacade;
import com.ricardopassarella.calldetails.dto.CallerDetails;
import com.ricardopassarella.caller.domain.dto.CallerReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ricardopassarella.common.MathUtils.divide;
import static com.ricardopassarella.common.MathUtils.getMedian;

@Service
@RequiredArgsConstructor
public class CallerReportService {

    private final CallDetailsFacade callDetailsFacade;

    Optional<CallerReportResponse> generateReport(String callerId, LocalDateTime from, LocalDateTime to) {
        var callerDetails = callDetailsFacade.getCallerDetail(callerId, from, to);

        if (callerDetails.isEmpty()) {
            return Optional.empty();
        }

        List<Long> durations = callerDetails.stream()
                                            .map(details -> ChronoUnit.SECONDS.between(details.getCallStart(), details.getCallEnd()))
                                            .sorted()
                                            .collect(Collectors.toList());

        BigDecimal totalCost = callerDetails.stream()
                                            .map(CallerDetails::getCost)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal median = getMedian(durations);

        long durationSum = durations.stream()
                                    .mapToLong(Long::longValue)
                                    .sum();

        BigDecimal averageDurationInSeconds = divide(durationSum, durations.size());

        long longerCall = durations.get(durations.size() - 1);

        return Optional.of(CallerReportResponse.builder()
                                               .averageDurationInSeconds(averageDurationInSeconds)
                                               .callVolume(durationSum)
                                               .totalCost(totalCost)
                                               .medianOfDurationInSeconds(median)
                                               .longerCallInSeconds(longerCall)
                                               .currency("GBP")
                                               .build());
    }

}
