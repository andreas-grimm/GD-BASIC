pipeline {
    agent { label 'java' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }
        stage('Deploy') {
            steps {
                sh 'cp ~/workspace/GD-Basic\\ unstable/target/BASIC-0.1.0-java21-jar-with-dependencies.jar /import/sol/work/Jenkins-Builds/Java/GD-Basic/GD-Basic.jar'
            }
        }
    }
    post {
        success {
            slackSend channel: '#jenkins', color: 'good', message: "SUCCESS: Job ${env.JOB_NAME} [Build #${env.BUILD_NUMBER}]"
        }
        failure {
            slackSend channel: '#jenkins', color: 'danger', message: "FAILURE: Job ${env.JOB_NAME} [Build #${env.BUILD_NUMBER}]"
        }
    }
}