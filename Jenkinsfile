def emailNotification (String developerEmail, String emailSubject='', String emailBody='', String attachments = '')
{
   emailext attachLog: true, attachmentsPattern: "$attachments", body: "$emailBody", subject: "$emailSubject", to: "$developerEmail"
}

node 
{
   cleanWs()
   def configFilePath = input message: 'Enter Configration Path', parameters: [string(defaultValue: '/home/ubuntu/confFile.properties', description: '', name: 'configFilePath', trim: true)]
   def props = readProperties  file: """${configFilePath}"""
   def create_infra = input message: '', parameters: [booleanParam(defaultValue: false, description: 'Check the if box you wish to create infrastructure for the job first', name: 'create_infra')]
   if ( """${create_infra}""" == true)
   {
        stage ('Confirmation to start the Job')
        {
	}

   }
   stage('Clone src code')
   {

   }
   stage('Clone role')
   {

   }
   stage('Update role')
   {

   }
   
   stage('Check If Application Present in Host')
   {

   }
   stage('Deploy app to Infrastructure and Configure Creds')
   {

   }
   stage('Test Application')
   {

   }
   stage('start CD job')
   {
      echo "Build CD Job"
   }

}
