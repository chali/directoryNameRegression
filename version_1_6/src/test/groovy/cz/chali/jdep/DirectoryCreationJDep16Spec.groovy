package cz.chali.jdep

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.vafer.jdeb.Compression
import org.vafer.jdeb.DebMaker
import org.vafer.jdeb.mapping.Mapper
import org.vafer.jdeb.mapping.PermMapper
import org.vafer.jdeb.producers.DataProducerPathTemplate
import spock.lang.Specification

class DirectoryCreationJDep16Spec extends Specification {

    @Rule
    TemporaryFolder folder= new TemporaryFolder()

    def 'add directory and confirm that is present'() {
        given:
        def directoryName = '/var/log/customdirectory'
        def dataProducer =  new DataProducerPathTemplate(
                [directoryName] as String[], null, null,
                [ new PermMapper(-1, -1, 'root', 'root', -1, -1, 0, null) ] as Mapper[])

        File debFile = folder.newFile('file.deb')

        DebMaker maker = new DebMaker(new ConsolePrinter(), [dataProducer], null)
        maker.setControl(new File(DirectoryCreationJDep16Spec.getResource('/control').toURI()).parentFile)
        maker.setDeb(debFile)
        maker.setCompression(Compression.GZIP.toString())

        when:
        maker.makeDeb()

        then:
        noExceptionThrown()

        def scan = new cz.chali.jdeb.Scanner(debFile)
        def emptydir = scan.getEntry(".$directoryName/")
        emptydir != null
    }
}
