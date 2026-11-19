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
<<<<<<< Updated upstream
                sh "cp ~/workspace/GD-Basic\\ unstable/target/BASIC-0.2.1.jar /import/sol/work/Jenkins-Builds/Java/GD-Basic/${env.GIT_BRANCH}/GD-Basic.jar"
=======
                sh 'cp ~/workspace/GD-Basic\\ unstable/target/BASIC-0.1.1-jar-with-dependencies.jar /import/sol/work/Jenkins-Builds/Java/GD-Basic/GD-Basic.jar'
>>>>>>> Stashed changes
            }
        }
    }
}