@Library('test-demo') _
node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   def application_name = 'attendance'
   def storage_app_name = 'my_sql'
   def application_role_name = 'attendence_deploy_role'
   def storage_app_role_name = 'mysql_role'
   def storage_app_instance_tag = 'test_mysql'
   def application_instance_tag = 'test_attendance'
   if ( """${create_infra}""" == true)
   {
      stage ('Confirmation to start the Job')
      {
         build 'ci_infra/infra_test_attendance'
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
         echo "Updating attendance_deploy_role"
         sh '''mysql_ip=$(python dynamic-inventory.py ${storage_app_instance_tag}) 
         sed -i "/host:/s/${storage_app_name}/${mysql_ip}/" ${application_role_name}/files/${application_name}/config.yaml'''
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
      deploy_role ('deploy_attendance.yml', prop[KEY_PATH], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      if ( """${create_infra}""" == true )
      {
         deploy_role ('deploy_mysql.yml', prop[KEY_PATH], props['DEVELOPEREMAIL'], props['SLACKCHANNELDEVELOPER'] )
      }
   }
   stage('Test Application')
   {
      echo "Bruh Do Something"
   }
   stage('start CD job')
   {
      echo """Build ${application_name} CD Job"""
      build job: '/cd_multibranch/prod_attendance', propagate: false, wait: false
   }

}
