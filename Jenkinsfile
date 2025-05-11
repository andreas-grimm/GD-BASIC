pipeline {
    agent { label 'java' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Deploy') {
            steps {
                sh 'cp ~/workspace/GD-Basic unstable/target/BASIC-0.1.0-java17-jar-with-dependencies.jar /import/sol/work/Jenkins-Builds/Java/GD-Basic/GD-Basic.jar'
            }
        }
    }
}