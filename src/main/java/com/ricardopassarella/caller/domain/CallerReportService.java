package com.ricardopassarella.caller.domain;

import com.ricardopassarella.calldetails.domain.CallDetailsFacade;
import com.ricardopassarella.caller.domain.dto.CallerReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.math.RoundingMode.DOWN;

@Service
@RequiredArgsConstructor
public class CallerReportService {

    private final CallDetailsFacade callDetailsFacade;

    Optional<CallerReportResponse> generateReport(String callerId, LocalDateTime from, LocalDateTime to) {
        var callerDetails = callDetailsFacade.getCallerDetail(callerId, from, to);

        if (callerDetails.isEmpty()) {
            return Optional.empty();
        }

        List<Pair<Long, BigDecimal>> pairList = callerDetails.stream()
                                                             .map(details -> {
                                                                 long duration = ChronoUnit.SECONDS.between(details.getCallStart(), details.getCallEnd());

                                                                 return Pair.of(duration, details.getCost());
                                                             })
                                                             .sorted(Comparator.comparing(Pair::getFirst))
                                                             .collect(Collectors.toList());

        return Optional.of(createCallerReportResponse(pairList));
    }

    private CallerReportResponse createCallerReportResponse(List<Pair<Long, BigDecimal>> pairList) {
        // TODO move this calculation to another class
        long sumDuration = 0;
        long longerCall = 0;
        BigDecimal sumCost = BigDecimal.ZERO;

        for (Pair<Long, BigDecimal> details : pairList) {
            sumDuration += details.getFirst();
            sumCost = sumCost.add(details.getSecond());

            if (details.getFirst() > longerCall) {
                longerCall = details.getFirst();
            }
        }

        int size = pairList.size();
        BigDecimal averageSeconds = BigDecimal.valueOf(sumDuration)
                                              .divide(BigDecimal.valueOf(size), 2, DOWN)
                                              .setScale(2, DOWN);
        BigDecimal median = getMedian(pairList);

        return CallerReportResponse.builder()
                                   .averageDurationInSeconds(averageSeconds)
                                   .callVolume(sumDuration)
                                   .totalCost(sumCost)
                                   .medianOfDurationInSeconds(median)
                                   .longerCallInSeconds(longerCall)
                                   .currency("GBP")
                                   .build();
    }

    private BigDecimal getMedian(List<Pair<Long, BigDecimal>> pairList) {
        int size = pairList.size();

        BigDecimal value = BigDecimal.valueOf(pairList.get(size / 2)
                                                      .getFirst())
                                     .setScale(2, DOWN);

        if (size % 2 == 0) {
            return value.add(BigDecimal.valueOf(pairList.get(size / 2 - 1)
                                                        .getFirst()))
                        .divide(BigDecimal.valueOf(2), 2, DOWN);
        }

        return value;
    }

}
