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
					echo "tratando de setear el id"
				     def id = createDeployment(getBranch(), "production", "Deploying branch to master")
				     echo "YA SETE EL ID"
				    setDeploymentStatus(id, "pending", "https://${herokuApp}.herokuapp.com/", "Pending deployment to");
				    echo "Entrando a herokuDeply"
				    herokuDeploy(herokuApp);
				    setDeploymentStatus(id, "success", "https://${herokuApp}.herokuapp.com/", "Successfully deployed to");
					
					echo "estoy por tarer el version"
					def version = getCurrentHerokuReleaseVersion("aplicacion-en-keroku")
					echo "estoy por tarer el ${version}"
					def createdAt = getCurrentHerokuReleaseDate("aplicacion-en-keroku", version)
					echo "Release version: ${version}"
					createRelease(version, createdAt)
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

void createRelease(tagName, createdAt) {
	echo "estoy createRelease"
    withCredentials([usernamePassword(credentialsId: '40654175-15aa-4c01-b97a-1a7757e599d8', usernameVariable: 'credencialGitHub', passwordVariable: 'GITHUB_TOKEN')]) {
        def body = "**Created at:** ${createdAt}\n**Deployment job:** [${env.BUILD_NUMBER}](${env.BUILD_URL})\n**Environment:** [aplicacion-en-keroku](https://dashboard.heroku.com/apps/aplicacion-en-keroku)"
        def payload = JsonOutput.toJson(["tag_name": "v${tagName}", "name": "aplicacion-en-keroku - v${tagName}", "body": "${body}"])
        def apiUrl = "https://api.github.com/repos/Flor21/IC2021}/releases"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
    }
}

def getCurrentHerokuReleaseVersion(app) {
	echo "estoy getCurrentHerokuReleaseVersion"
    withCredentials([sshUserPrivateKey(credentialsId: '66213ced-1975-435d-874e-61038630eefa', keyFileVariable: 'HEROKU_API_KEY')]) {
        def apiUrl = "https://api.heroku.com/apps/${app}/dynos"
        def response = sh(returnStdout: true, script: "curl -s  -H \"Authorization: Bearer ${env.HEROKU_API_KEY}\" -H \"Accept: application/vnd.heroku+json; version=3\" -X GET ${apiUrl}").trim()
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parseText("${response}")
        return data[0].release.version
    }
}

def getCurrentHerokuReleaseDate(app, version) {
	echo "estoy getCurrentHerokuReleaseDate"
    withCredentials([sshUserPrivateKey(credentialsId: '66213ced-1975-435d-874e-61038630eefa', keyFileVariable: 'HEROKU_API_KEY')]) {
        def apiUrl = "https://api.heroku.com/apps/${app}/releases/${version}"
        def response = sh(returnStdout: true, script: "curl -s  -H \"Authorization: Bearer ${env.HEROKU_API_KEY}\" -H \"Accept: application/vnd.heroku+json; version=3\" -X GET ${apiUrl}").trim()
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parseText("${response}")
        return data.created_at
    }
}

void setDeploymentStatus(deploymentId, state, targetUrl, description) {
	echo "estoy setDeploymentStatus"
    withCredentials([usernamePassword(credentialsId: '40654175-15aa-4c01-b97a-1a7757e599d8', usernameVariable: 'credencialGitHub', passwordVariable: 'GITHUB_TOKEN')]) {
        def payload = JsonOutput.toJson(["state": "${state}", "target_url": "${targetUrl}", "description": "${description}"])
        def apiUrl = "https://api.github.com/repos/Flor21/IC2021/deployments/${deploymentId}/statuses"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
        echo "saliendo setDeploymentStatus"
    }
}
def createDeployment(ref, environment, description) {
	echo "estoy createDeployment"
    withCredentials([usernamePassword(credentialsId: '40654175-15aa-4c01-b97a-1a7757e599d8', usernameVariable: 'credencialGitHub', passwordVariable: 'GITHUB_TOKEN')]) {
        def payload = JsonOutput.toJson(["ref": "${ref}", "description": "${description}", "environment": "${environment}", "required_contexts": []])
        echo "estoy por entrar a apiURL"
        def apiUrl = "https://api.github.com/repos/Flor21/IC2021/deployments"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
        echo "${response}"
        def jsonSlurper = new JsonSlurper()
        echo "INSTANCIADO JsonSlurper"
        def data = jsonSlurper.parseText("${response}")
        echo "JsonSlurper ${data}"
        return data.id
    }
}
def getBranch() {
    tokens = "${env.JOB_NAME}".tokenize('/')
    branch = tokens[tokens.size()-1]
    return "${branch}"
}
def herokuDeploy (herokuApp) {
	echo "HEROKUAPP ${herokuApp}"
    withCredentials([sshUserPrivateKey(credentialsId: '66213ced-1975-435d-874e-61038630eefa', keyFileVariable: 'HEROKU_API_KEY')]) {
    echo "HEROKUAPP ${keyFileVariable}"
       sh 'mvn clean -U install'
       sh 'mvn clean heroku:deploy' 
    }
}
