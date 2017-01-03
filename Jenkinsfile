properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', artifactDaysToKeepStr: '-1', artifactNumToKeepStr: '10', daysToKeepStr: '-1', numToKeepStr: '10']]])

node {
    timestamps {
        stage('PreCleanup') {
           step($class: 'WsCleanup')
        }
        stage('Checkout') {
            checkout scm
        }

        withMaven(maven: 'MVN-3', jdk: 'JDK-8', mavenLocalRepo: '.repository') {
            stage('Build') {
                sh 'mvn -e install site -DskipTests'
            }
            stage('Test') {
                sh 'mvn -e test'
                junit '**/target/*/TEST-*.xml'
            }
        }

        stage('Archive') {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
        stage('PostCleanup') {
           step($class: 'WsCleanup')
        }
    }
}