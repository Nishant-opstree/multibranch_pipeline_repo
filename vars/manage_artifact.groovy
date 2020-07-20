def artifact_push(String application_name, String bucket_name, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
    try
    {
        echo "Upload new artifact to s3"
        def artifact_name = """${application_name}-${BUILD_TIMESTAMP}""" 
        sh """aws s3 sync ${application_name}_src s3://${bucket_name}/${application_name}/${artifact_name}"""
    }
    catch (err)
    {
        emailext attachLog: true, body: 'Build-URL: "${BUILD_URL}"', subject: """${inventory_name} Was Not able get executed""", to: """{DEVELOPEREMAIL}"""
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The code Was Not able get deployed Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}

def artifact_pull(String application_name, String bucket_name, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
    try
    {
        echo "Show options new artifact to s3"
        node {
    stage('Preparation') {
     input message: '', parameters: [extendedChoice(bindings: '', defaultValue: '', description: '', descriptionPropertyValue: '', groovyClasspath: '', groovyScript: """def sout = new StringBuilder(), serr = new StringBuilder()
def proc = \'aws s3 ls s3://${bucket_name}/${application_name}/\'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(2000)
def values = "$sout".split(\'/\')
def trimmedValues
def parameters=[]
values.each {  println "${it}" }
def cleanValues = "$sout".split(\'PRE\')
def last = cleanValues.last().split(\'2018-12-17\')[0]
cleanValues.each {  "${it}".toString(); 
                    trimmedValues = "${it}".trim();
                    parameters<<trimmedValues
                 }
parameters.remove(parameters.size() - 1);
parameters.add(last)
parameters""", multiSelectDelimiter: ',', name: '', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_SINGLE_SELECT', visibleItemCount: 5)]
    }
}

        sh """sudo rm -r ${application_name}_src/*"""
        sh """aws s3 sync s3://${bucket_name}/${application_name}/${artifact_name} ${application_name}_src"""
    }
    catch (err)
    {
        emailext attachLog: true, body: 'Build-URL: "${BUILD_URL}"', subject: """${inventory_name} Was Not able get executed""", to: """{DEVELOPEREMAIL}"""
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The code Was Not able get deployed Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}