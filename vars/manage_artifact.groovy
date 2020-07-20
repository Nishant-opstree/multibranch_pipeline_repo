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

def artifact_push(String application_name, String bucket_name, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
    try
    {
        echo "Show options new artifact to s3"
        def artifact_name_list = sh (script:"""bash artifact_list.sh ${bucket_name} ${application_name}""", returnStdout: true).trim()
        input message: '', parameters: [extendedChoice(description: '', multiSelectDelimiter: ',', name: '', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_SINGLE_SELECT', value: """${artifact_name_list}""", visibleItemCount: 5)]
        sh """sudo rm -r ${application_name}_src"""
        sh """aws s3 sync s3://${bucket_name}/${application_name}/${artifact_name} ${application_name}_src"""
    }
    catch (err)
    {
        emailext attachLog: true, body: 'Build-URL: "${BUILD_URL}"', subject: """${inventory_name} Was Not able get executed""", to: """{DEVELOPEREMAIL}"""
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The code Was Not able get deployed Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}