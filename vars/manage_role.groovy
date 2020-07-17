#!/usr/bin/env groovy
def clone(String gitUrl, String role_name, String application_name, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
    try
    {
        echo "Cloning role"
        git branch: """${application_name}""", credentialsId: 'nishant_github_account', url: """${gitUrl}"""
        sh """
        mkdir -p ${role_name}/files/${application_name}
        mv -f ${application_name}_src/* ${role_name}/files/${application_name}/"""
        
    }
    catch (err)
    {
        emailNotification ( """${DEVELOPEREMAIL}""", 'The role Was Not able to get cloned', 'Build-URL: "${BUILD_URL}"' )
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The cloning of project was not successful Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}