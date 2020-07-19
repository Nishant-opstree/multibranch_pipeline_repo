@Library('test-demo') _
node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   def application_name = 'employee'
   def storage_app_name = 'elasticsearch'
   def application_role_name = 'employee_role'
   def storage_app_role_name = 'elasticsearch_role'
   def storage_app_instance_tag = 'elasticsearch'
   def application_instance_tag = 'employee'
   def application_initiate_yaml = 'deploy_employee.yml'
   def storage_app_initiate_yaml = 'deploy_elasticsearch.yml'
   if ( """${create_infra}""" == true)
   {
      stage ('Confirmation to start the Job')
      {
         build job: 'infrastructure_pipeline', parameters: [string(name: 'environment', value: 'prod')]
	   }
   }
   stage('Clone src code')
   {
      def gitUrl = "https://github.com/Nishant-opstree/ot-microservices.git" 
      clone_src_code ( """${gitUrl}""", """${application_name}""" , props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
   }
   stage('Clone role')
   {
      def gitUrl = "https://github.com/Nishant-opstree/roles.git"
      manage_role.clone ("""${gitUrl}""", """${application_role_name}""", """${application_name}""", props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'])
   }
   stage('Update role')
   {
      try
      {
         echo "Updating employee_role"
         def elasticsearch_ip = sh (script:"""python dynamic-inventory.py ${storage_app_instance_tag}""", returnStdout: true).trim()
         sh """sed -i "/host:/s/${storage_app_name}/${elasticsearch_ip}/" ${application_role_name}/files/${application_name}/config.yaml"""
      }
      catch (err)
      {
         emailNotification ( """${developerEmail}""", 'Role was not updated', 'Build-URL: "${BUILD_URL}"' )
         sh "exit 1"
      }
   }
   
   stage('Check If Application Present in Host')
   {
      echo "checking if Application is present in host and if present taking its "
   }
   stage('Deploy app to Infrastructure and Configure Creds')
   {
      deploy_role ("""${application_instance_tag}""", """${application_initiate_yaml}""", props['KEY_PATH'], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      if ( """${create_infra}""" == true )
      {
         deploy_role ("""${storage_app_instance_tag}""", """${storage_app_initiate_yaml}""", props['KEY_PATH'], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      }
   }
   stage('Test Application')
   {
      echo "Bruh Do Something"
   }


}
