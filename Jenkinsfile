@Library('test-demo') _
node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   def application_name = 'frontend'
   def application_role_name = 'frontend_role'
   def application_instance_tag = 'test_frontend'
   def application_initiate_yaml = 'deploy_frontend.yml'
   def gateway_instance_tag = 'test_gateway'
   if ( """${create_infra}""" == true)
   {
      stage ('Confirmation to start the Job')
      {
         build job: 'infrastructure_pipeline', parameters: [string(name: 'environment', value: 'test'), string(name: 'branch', value: 'test_frontend')]
	   }
   }
   stage('Clone src code')
   {
      def gitUrl = "https://github.com/Nishant-opstree/ot-microservices.git" 
      clone_src_code ( """${gitUrl}""", """${application_name}""" , props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      sh """cp ${application_name}/.env ${application_name}_src/"""
   }
   stage('Clone role')
   {
      def gitUrl = "https://github.com/Nishant-opstree/roles.git"
      manage_role.clone ("""${gitUrl}""", """${application_role_name}""", """${application_name}""", props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'])
      sh """cp frontend_src/.env frontend_role/files/frontend/"""
   }
   stage('Update role')
   {
      try
      {
         echo "Updating frontend_role"
         def gateway_ip = sh (script:"""python dynamic-inventory.py ${gateway_instance_tag}""", returnStdout: true).trim()
         sh """sed -i "/REACT_APP_GATEWAY_URL:/s/gateway/${gateway_ip}/" ${application_role_name}/files/${application_name}/.env"""
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
   }
   stage('Test Application')
   {
      echo "Bruh Do Something"
   }
   stage('start CD job')
   {
      echo """Build ${application_name} CD Job"""
      build job: '/cd_pipeline/prod_frontend', propagate: false, wait: false
   }

}
