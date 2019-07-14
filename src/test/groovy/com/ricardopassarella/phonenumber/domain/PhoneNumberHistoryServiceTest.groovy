package com.ricardopassarella.phonenumber.domain

import com.ricardopassarella.calldetails.domain.CallDetailsFacade
import com.ricardopassarella.calldetails.dto.CallLog
import com.ricardopassarella.common.PageableResponse
import com.ricardopassarella.phonenumber.domain.dto.PhoneNumberHistory
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhoneNumberHistoryServiceTest extends Specification {

    def callDetailsFacade = Mock CallDetailsFacade

    PhoneNumberHistoryService service

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    void setup() {
        service = new PhoneNumberHistoryService(callDetailsFacade)
    }

    @Unroll
    def "when get a phoneNumber history, should call callDetailsFacade"() {
        given:

            def expected = new PageableResponse(expectedCallerHistory, count)

        when:
            def history = service.getCallLogHistory("123", LocalDateTime.now(), LocalDateTime.now(), 2, 0)

        then:
            1 * callDetailsFacade.getCallLogByPhoneNumber(_, _, _, _, _) >> callLog
            1 * callDetailsFacade.getCallLogCountByPhoneNumber(_, _, _) >> count

        and:
            history == expected

        where:
            callLog << [
                    [],
                    [callLog("2016-08-01 08:18:37", "2016-08-01 08:20:10", "7111111111", "123")]
            ]

            expectedCallerHistory << [
                    [],
                    [callerHistory("2016-08-01 08:18:37", "2016-08-01 08:20:10", "7111111111", "123")]
            ]

            count = 4
    }

    private static CallLog callLog(String callStart, String callEnd, String recipient, String callerId) {
        CallLog.builder()
               .callStart(LocalDateTime.parse(callStart, dateTimeFormatter))
               .callEnd(LocalDateTime.parse(callEnd, dateTimeFormatter))
               .recipient(recipient)
               .callerId(callerId)
               .build()
    }

    private static PhoneNumberHistory callerHistory(String callStart, String callEnd, String recipient, String callerId) {
        PhoneNumberHistory.builder()
                          .callStart(LocalDateTime.parse(callStart, dateTimeFormatter))
                          .callEnd(LocalDateTime.parse(callEnd, dateTimeFormatter))
                          .recipient(recipient)
                          .callerId(callerId)
                          .build()
    }
}
