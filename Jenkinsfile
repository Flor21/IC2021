pipeline{
    agent any
    stages{
        stage('Build'){
            steps{
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test'){
            steps{
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver'){
        	steps{
                sh './script/deliver.sh'
               } 
        }
        stage('Slack Notification'){
            steps{
                script{
                    try{
                        slackSend(channel: "#continuous-integration")
                    }
                    catch(Exception error){
                        sh 'echo "job failed"'
                    }
                }
            }
        }
    }
}
