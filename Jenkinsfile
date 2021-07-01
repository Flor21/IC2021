import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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
        stage('Deploy'){
        	steps{
	        	script{
	        		herokuApp = "aplicacion-en-keroku"
					step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
				     def id = createDeployment(getBranch(), "production", "Deploying branch to master")
				    setDeploymentStatus(id, "pending", "https://${herokuApp}.herokuapp.com/", "Pending deployment to");
				    herokuDeploy(herokuApp);
				    setDeploymentStatus(id, "success", "https://${herokuApp}.herokuapp.com/", "Successfully deployed to");
				}
        	}
        }
        stage('Slack Notification'){
            steps{
                script{
                    try{
                        slackSend(baseUrl: "https://hooks.slack.com/services/", teamDomain: "https://teamwork-nw17640.slack.com", channel: "#continuous-integration", color: "good", message: "Se ha deployado correctamente", tokenCredentialId: "slack-demo", username: "Team work", botUser: true, timestamp: slackResponse.ts)
                        echo "Se ha deployado corretamente"
                    }
                    catch(Exception error){
                        echo "Ocurrio un error cuando sea queria deployar"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
    }
}


void setDeploymentStatus(deploymentId, state, targetUrl, description) {
    withCredentials([usernamePassword(credentialsId: '40654175-15aa-4c01-b97a-1a7757e599d8', usernameVariable: 'credencialGitHub', passwordVariable: 'GITHUB_TOKEN')]) {
        def payload = JsonOutput.toJson(["state": "${state}", "target_url": "${targetUrl}", "description": "${description}"])
        def apiUrl = "https://api.github.com/repos/Flor21/IC2021/deployments/${deploymentId}/statuses"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
    }
}
def createDeployment(ref, environment, description) {
    withCredentials([usernamePassword(credentialsId: '40654175-15aa-4c01-b97a-1a7757e599d8', usernameVariable: 'credencialGitHub', passwordVariable: 'GITHUB_TOKEN')]) {
        def payload = JsonOutput.toJson(["ref": "${ref}", "description": "${description}", "environment": "${environment}", "required_contexts": []])
        def apiUrl = "https://api.github.com/repos/Flor21/IC2021/deployments"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parseText("${response}")
        return data.id
    }
}
def getBranch() {
    tokens = "${env.JOB_NAME}".tokenize('/')
    branch = tokens[tokens.size()-1]
    return "${branch}"
}
def herokuDeploy (herokuApp) {
	sh 'mvn clean -U install'
	sh 'mvn heroku:deploy -Dheroku.appName=aplicacion-en-keroku' 
}
