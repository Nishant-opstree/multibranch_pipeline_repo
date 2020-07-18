@Library('test-demo') _
node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   def application_name = 'gateway'
   def application_role_name = 'gateway_role'
   def application_instance_tag = 'gateway'
   def application_initiate_yaml = 'deploy_gateway.yml'
   if ( """${create_infra}""" == true)
   {
      stage ('Confirmation to start the Job')
      {
         build job: 'infrastructure_pipeline', parameters: [string(name: 'environment', value: 'prod'), string(name: 'branch', value: 'prod_gateway')]
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
         echo "Updating gateway_role"
         sh '''attendance_ip=$(python dynamic-inventory.py attendance) 
         sed -i "/attendance:8081/s/attendance/${attendance_ip}/" ${application_role_name}/files/${application_name}/src/main/resources/application.yml
         employee_ip=$(python dynamic-inventory.py employee) 
         sed -i "/employee:8083/s/employee/${employee_ip}/" ${application_role_name}/files/${application_name}/src/main/resources/application.yml
         salary_ip=$(python dynamic-inventory.py salary) 
         sed -i "/salary:8082/s/salary/${salary_ip}/" ${application_role_name}/files/${application_name}/src/main/resources/application.yml'''
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
   }
   stage('Test Application')
   {
      echo "Bruh Do Something"
   }

}