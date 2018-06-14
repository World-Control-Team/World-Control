pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew setupCIWorkspace'
        sh './gradlew build'
      }
    }
    stage('Archival') {
      steps {
        archiveArtifacts '**/build/libs/*.jar'
      }
    }
  }
}