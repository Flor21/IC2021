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
	        		herokuApp = "<aplicacion-en-keroku>"
					step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
					deployToStage("production", herokuApp)
					def version = getCurrentHerokuReleaseVersion("<aplicacion-en-keroku>")
					def createdAt = getCurrentHerokuReleaseDate("<aplicacion-en-keroku>", version)
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
    withCredentials([[$class: 'StringBinding', credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN']]) {
        def body = "**Created at:** ${createdAt}\n**Deployment job:** [${env.BUILD_NUMBER}](${env.BUILD_URL})\n**Environment:** [<aplicacion-en-keroku>](https://dashboard.heroku.com/apps/<aplicacion-en-keroku>)"
        def payload = JsonOutput.toJson(["tag_name": "v${tagName}", "name": "<aplicacion-en-keroku> - v${tagName}", "body": "${body}"])
        def apiUrl = "https://api.github.com/repos/${getRepoSlug()}/releases"
        def response = sh(returnStdout: true, script: "curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}").trim()
    }
}
