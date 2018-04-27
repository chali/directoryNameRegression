package cz.chali.jdep

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.vafer.jdeb.Compression
import org.vafer.jdeb.DebMaker
import org.vafer.jdeb.mapping.Mapper
import org.vafer.jdeb.mapping.PermMapper
import org.vafer.jdeb.producers.DataProducerLink
import org.vafer.jdeb.producers.DataProducerPathTemplate
import spock.lang.Specification

class TooLongLinkSpec extends Specification {

    @Rule
    TemporaryFolder folder= new TemporaryFolder()

    def 'too long links should be allowed'() {
        given:
        def directoryName = "/var/log/" + longPathSuffix()
        def dataProducer =  new DataProducerPathTemplate(
                [directoryName] as String[], null, null,
                [ new PermMapper(-1, -1, 'root', 'root', -1, -1, 0, null) ] as Mapper[])

        def linkProducer = new DataProducerLink('/var/log/short', directoryName, true, null, null, null)

        File debFile = folder.newFile('file.deb')

        DebMaker maker = new DebMaker(new ConsolePrinter(), [dataProducer, linkProducer], null)
        maker.setControl(new File(TooLongLinkSpec.getResource('/control').toURI()).parentFile)
        maker.setTarLongFileMode("posix")
        maker.setDeb(debFile)
        maker.setCompression(Compression.GZIP.toString())

        when:
        maker.makeDeb()

        then:
        noExceptionThrown()

        def scan = new cz.chali.jdeb.Scanner(debFile)
        scan.getEntry("./$directoryName/")
    }

    String longPathSuffix() {
        def builder = new StringBuilder()
        (1..20).each {
            builder.append('loooongname')
        }
        builder.toString()
    }
}
