package com.ricardopassarella.calldetails.domain

import com.ricardopassarella.calldetails.domain.exception.FailedToParseUploadCallDetails
import com.ricardopassarella.calldetails.domain.exception.UnknownCurrencyException
import com.ricardopassarella.calldetails.dto.CallFinanceInsert
import com.ricardopassarella.calldetails.dto.CallLogInsert
import com.ricardopassarella.calldetails.dto.ExchangeRate
import com.ricardopassarella.infrastructure.csv.CsvConfig
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

class CallDetailsImportServiceTest extends Specification {

    def repository = Mock CallDetailsRepository

    @Subject
    CallDetailsImportService service

    void setup() {
        def config = new CsvConfig()

        def routines = config.csvRoutines()
        service = new CallDetailsImportService(
                routines,
                repository
        )
    }

    @Unroll
    def "when upload the csv #input then we should insert in batches #numberOfInserts times"() {
        given:
            service.BATCH_SIZE = batchSize
            MockMultipartFile multipartFile = getMultipartFile(filePath)
        and:
            repository.getExchangeRate("GBP") >> Optional.of(ExchangeRate.builder()
                                                                         .exchangeRate(BigDecimal.ONE)
                                                                         .localCurrency("GBP")
                                                                         .build())
            List<CallLogInsert> callLogs
            List<CallFinanceInsert> callerFinances
        when:
            service.parseAndInsertCallDetails(multipartFile)

        then:
            numberOfInserts * repository.insertBatch(_, _) >> { it1, it2 ->
                callLogs = it1
                callerFinances = it2
            }
            callLogs.size() == sizeOfLastInsert
            callerFinances.size() == sizeOfLastInsert

        where:
            batchSize | filePath                  | numberOfInserts | sizeOfLastInsert
            100       | "/csv-with-30-lines.csv"  | 1               | 30
            100       | "/csv-with-120-lines.csv" | 2               | 20

    }

    def "when upload a csv with unknown currency, should throw UnknownCurrencyException"() {
        given:
            MockMultipartFile multipartFile = getMultipartFile("/csv-with-30-lines.csv")

        and:
            repository.getExchangeRate("GBP") >> Optional.empty()

        when:
            service.parseAndInsertCallDetails(multipartFile)

        then:
            def e = thrown(UnknownCurrencyException)
            e.getMessage() == "Currency 'GBP' not supported with reference C5DA9724701EEBBA95CA2CC5617BA93E4"
    }

    def "when upload a csv with invalid data, should thrown FailedToParseUploadCallDetails"() {
        given:
            MockMultipartFile multipartFile = getMultipartFile(filePath)

        when:
            service.parseAndInsertCallDetails(multipartFile)
        then:
            thrown(FailedToParseUploadCallDetails)

        where:
            filePath << [
                    "/csv-with-call-date-in-unknown-format.csv",
                    "/csv-with-end-time-in-unknown-format.csv"
            ]
    }

    private static MockMultipartFile getMultipartFile(String filePath) {
        def file = CallDetailsImportServiceTest.class.getResource(filePath).getFile()
        byte[] bytes = Files.readAllBytes(Paths.get(file))
        def multipartFile = new MockMultipartFile("csv", bytes)
        multipartFile
    }
}
