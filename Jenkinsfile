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
                sh "cp ~/workspace/GD-Basic\\ unstable/target/BASIC-0.2.1.jar /import/sol/work/Jenkins-Builds/Java/GD-Basic/${env.GIT_BRANCH}/GD-Basic.jar"
            }
        }
    }
}