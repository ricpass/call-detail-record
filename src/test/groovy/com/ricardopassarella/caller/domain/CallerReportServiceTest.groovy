package com.ricardopassarella.caller.domain

import com.ricardopassarella.calldetails.domain.CallDetailsFacade
import com.ricardopassarella.calldetails.dto.CallerDetails
import com.ricardopassarella.caller.domain.dto.CallerReportResponse
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CallerReportServiceTest extends Specification {

    def callDetailsFacade = Mock CallDetailsFacade

    @Subject
    CallerReportService service

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    void setup() {
        service = new CallerReportService(callDetailsFacade)
    }

    @Unroll
    def "when creating a caller report, should return values"() {
        given:

            callDetailsFacade.getCallerDetail(_, _, _) >> callerDetails

        when:
            def report = service.generateReport("123", LocalDateTime.now(), LocalDateTime.now())

        then:
            report == expectedReport

        where:
            callerDetails << [
                    [],
                    [
                            createCallerDetails("2016-08-01 08:18:37", "2016-08-01 08:20:10", 0.085)
                    ],
                    [
                            createCallerDetails("2016-08-01 08:18:37", "2016-08-01 08:20:10", 0.085),
                            createCallerDetails("2016-08-01 11:58:36", "2016-08-01 11:59:48", 6.414),
                            createCallerDetails("2016-08-01 12:53:04", "2016-08-01 12:53:56", 5.828),
                            createCallerDetails("2016-08-01 10:53:01", "2016-08-01 10:53:37", 5.825),
                    ],
                    [
                            createCallerDetails("2016-08-01 08:18:37", "2016-08-01 08:20:10", 0.085),
                            createCallerDetails("2016-08-01 08:18:37", "2016-08-01 08:20:10", 0.085),
                            createCallerDetails("2016-08-01 11:58:36", "2016-08-01 11:59:48", 6.414),
                            createCallerDetails("2016-08-01 12:53:04", "2016-08-01 12:53:56", 5.828),
                            createCallerDetails("2016-08-01 10:53:01", "2016-08-01 10:53:37", 5.825),
                    ]
            ]

            expectedReport << [
                    Optional.empty(),
                    Optional.of(
                            CallerReportResponse.builder()
                                                .averageDurationInSeconds(93.00)
                                                .medianOfDurationInSeconds(93.00)
                                                .longerCallInSeconds(93)
                                                .callVolume(93)
                                                .totalCost(0.085)
                                                .currency("GBP")
                                                .build()
                    )
                    ,
                    Optional.of(
                            CallerReportResponse.builder()
                                                .averageDurationInSeconds(63.25)
                                                .medianOfDurationInSeconds(62.00)
                                                .longerCallInSeconds(93)
                                                .callVolume(253)
                                                .totalCost(18.152)
                                                .currency("GBP")
                                                .build()
                    ),
                    Optional.of(
                            CallerReportResponse.builder()
                                                .averageDurationInSeconds(69.20)
                                                .medianOfDurationInSeconds(72.00)
                                                .callVolume(346)
                                                .longerCallInSeconds(93)
                                                .totalCost(18.237)
                                                .currency("GBP")
                                                .build()
                    )
            ]

    }

    private static CallerDetails createCallerDetails(String callStart, String callEnd, BigDecimal cost) {
        CallerDetails.builder()
                     .callStart(LocalDateTime.parse(callStart, dateTimeFormatter))
                     .callEnd(LocalDateTime.parse(callEnd, dateTimeFormatter))
                     .cost(cost)
                     .build()

    }
}
