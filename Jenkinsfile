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
                sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
                sh 'java -jar target/ejercicio:prueba:jar:0.0.1-SNAPSHOT.jar'
               } 
        }
    }
}
