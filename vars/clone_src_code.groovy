#!/usr/bin/env groovy
def call(String gitUrl, String application_name, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
	try
    {
        echo "Cloning Code From Remote Repo"
        git credentialsId: 'nishant_github_account', url: """${gitUrl}"""
        sh """ mkdir ${application_name}_src
        mv -f ${application_name}/* ${application_name}_src/"""
    }
    catch (err)
    {
        emailext attachLog: true, body: 'Build-URL: "${BUILD_URL}"', subject: 'The code Was Not able to get cloned', to: """{DEVELOPEREMAIL}"""
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The cloning of project was not successful Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}

