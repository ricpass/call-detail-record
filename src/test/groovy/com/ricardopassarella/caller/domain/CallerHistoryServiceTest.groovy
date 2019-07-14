package com.ricardopassarella.caller.domain

import com.ricardopassarella.calldetails.domain.CallDetailsFacade
import com.ricardopassarella.calldetails.dto.CallLog
import com.ricardopassarella.caller.domain.dto.CallerHistory
import com.ricardopassarella.common.PageableResponse
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CallerHistoryServiceTest extends Specification {

    def callDetailsFacade = Mock CallDetailsFacade

    CallerHistoryService service

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    void setup() {
        service = new CallerHistoryService(callDetailsFacade)
    }

    @Unroll
    def "when get caller history, should call callDetailsFacade"() {
        given:

            def expected = new PageableResponse(expectedCallerHistory, count)

        when:
            def history = service.getCallLogHistory("123", LocalDateTime.now(), LocalDateTime.now(), 2, 0)

        then:
            1 * callDetailsFacade.getCallLog(_, _, _, _, _) >> callLog
            1 * callDetailsFacade.getCallLogCount(_, _, _) >> count

        and:
            history == expected

        where:
            callLog << [
                    [],
                    [callLog("2016-08-01 08:18:37", "2016-08-01 08:20:10", "7111111111")]
            ]

            expectedCallerHistory << [
                    [],
                    [callerHistory("2016-08-01 08:18:37", "2016-08-01 08:20:10", "7111111111")]
            ]

            count = 4
    }

    private static CallLog callLog(String callStart, String callEnd, String recipient) {
        CallLog.builder()
               .callStart(LocalDateTime.parse(callStart, dateTimeFormatter))
               .callEnd(LocalDateTime.parse(callEnd, dateTimeFormatter))
               .recipient(recipient)
               .build()
    }

    private static CallerHistory callerHistory(String callStart, String callEnd, String recipient) {
        CallerHistory.builder()
                     .callStart(LocalDateTime.parse(callStart, dateTimeFormatter))
                     .callEnd(LocalDateTime.parse(callEnd, dateTimeFormatter))
                     .recipient(recipient)
                     .build()
    }
}
