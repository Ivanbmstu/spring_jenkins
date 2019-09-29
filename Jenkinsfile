//version 0.0.1
def rtGradle
def gradleTasks = ''
def UUID_DIR = UUID.randomUUID().toString()

String deployRepo, repoSlug, projKey, branch, version

pipeline {
  agent {
    label {
      label 'local-jenkins-new'
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
    string(name: 'branch', description: 'branch to build', defaultValue: 'master')
    string(name: 'task_id', description: 'task_id for tracing', defaultValue: '')
    string(name: 'chain_id', description: 'chaind for fetch deployment info', defaultValue: '')
    string(name: 'artifact_target_type', description: 'RELEASE|SNAPSHOT|BUILD', defaultValue: 'BUILD')
    string(name: 'commit_from', description: 'start commit', defaultValue: '')
    string(name: 'commit_to', description: 'end commit', defaultValue: '')
  }

  stages {
    stage('define build method') {
      steps {
        sh './gradlew build'
      }
    }
  }
}
