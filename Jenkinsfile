//version 0.0.1
def gradleTasks = ''
def UUID_DIR = UUID.randomUUID().toString()

String deployRepo, repoSlug, projKey, branch, version

pipeline {
  agent {
    label {
      label 'local-jenkins'
      customWorkspace UUID_DIR
    }
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '15', artifactNumToKeepStr: '1'))
    timeout(time: 30, unit: 'MINUTES')
    skipStagesAfterUnstable()
    timestamps()
  }

  stages {
    stage('define build method') {
      steps {
        script {
              deployRepo  = ''
              gradleTasks = 'build'
        }
      }
    }
    stage('build') {
      steps {
          script {
             sh './gradlew clean build --info'
          }
      }
    }
  }
}

def markAsUnstable() {
  currentBuild.result='UNSTABLE'
}
def notifyBitbucketWithState(String state) {
  if('SUCCESS' == state || 'FAILED' == state) {
    currentBuild.result = state         // Set result of currentBuild !Important!
  }
  notifyBitbucket()
}
