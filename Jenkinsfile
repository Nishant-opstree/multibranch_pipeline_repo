@Library('test-demo') _
node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   def application_name = 'webserver'
   def application_role_name = 'webserver_setup'
   def application_instance_tag = 'webserver'
   def application_initiate_yaml = 'deploy_webserver.yml'
   if ( """${create_infra}""" == true)
   {
      stage ('Confirmation to start the Job')
      {
         build job: 'infrastructure_pipeline', parameters: [string(name: 'environment', value: 'prod'), string(name: 'branch', value: 'prod_webserver')]
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
         echo "Updating webserver_role"
         sh '''frontend_ip=$(python dynamic-inventory.py frontend) 
         sed -i "/frontend:5000/s/frontend/${frontend_ip}/" ${application_role_name}/files/${application_name}/nginx.conf
         gateway_ip=$(python dynamic-inventory.py gateway) 
         sed -i "/gateway:8080/s/gateway/${gateway_ip}/" ${application_role_name}/files/${application_name}/nginx.conf
         '''
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
      deploy_role ("""${application_initiate_yaml}""", prop[KEY_PATH], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      if ( """${create_infra}""" == true )
      {
         deploy_role ("""${storage_app_initiate_yaml}""", prop[KEY_PATH], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      }
   }
   stage('Test Application')
   {
      echo "Bruh Do Something"
   }

}
