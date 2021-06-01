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

  parameters {
    string(name: 'branch', description: 'branch to build', defaultValue: 'java_only')
    string(name: 'task_id', description: 'task_id for tracing', defaultValue: '')
    string(name: 'chain_id', description: 'chaind for fetch deployment info', defaultValue: '')
    string(name: 'artifact_target_type', description: 'RELEASE|SNAPSHOT|BUILD', defaultValue: 'BUILD')
    string(name: 'commit_from', description: 'start commit', defaultValue: '')
    string(name: 'commit_to', description: 'end commit', defaultValue: '')
  }

  stages {
    stage('define build method') {
      steps {
        script {
          switch(params.artifact_target_type) {
            case 'RELEASE':
              gradleTasks = ''
              deployRepo  = 'build'
            break
            case 'SNAPSHOT':
              deployRepo  = ''
              gradleTasks = 'build'
            break
            case 'BUILD':
              deployRepo  = ''
              gradleTasks = 'build'
            break
            default:
              markAsUnstable()
            break
          }
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
