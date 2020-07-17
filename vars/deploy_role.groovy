def call(String inventory_name, String key_path, String DEVELOPEREMAIL, String SLACKCHANNELDEVELOPER)
{
    try
    {
        echo "Deploying Attendance and mysql code"
        sh """
            bash create_inventory.sh test_attendance ${key_path}
            ansible-playbook -i inventory ${inventory_name}
            rm inventory
        """
    }
    catch (err)
    {
        emailext attachLog: true, body: 'Build-URL: "${BUILD_URL}"', subject: """${inventory_name} Was Not able get executed""", to: """{DEVELOPEREMAIL}"""
        //slackSend channel: """${SLACKCHANNELDEVELOPER}""", message: 'The code Was Not able get deployed Build-URL: "${BUILD_URL}" '
        sh "exit 1"
    }
}