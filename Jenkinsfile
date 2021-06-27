def slackResponse = slackSend(channel: "#continuous-integration", message: "Se ha deployado correctamente")
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
                        slackSend({baseUrl: "https://hooks.slack.com/services/"}, {teamDomain: "https://teamwork-nw17640.slack.com"}, {channel: "#continuous-integration"}, {color: "good"}, {message: "Se ha deployado correctamente"}, {tokenCredentialId: "slack-demo"}, {username: "Team work"}, {botUser: true}, {timestamp: slackResponse.ts})
                        echo "bien"
                    }
                    catch(Exception error){
                        echo "fallo"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
    }
}
