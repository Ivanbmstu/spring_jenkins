//version 0.0.1
def rtGradle
def gradleTasks = ''
def buildInfo = Artifactory.newBuildInfo()
def UUID_DIR = UUID.randomUUID().toString()

String deployRepo, repoSlug, projKey, branch, version

pipeline {
  agent {
    label {
      label 'local-jenkins-n16-j15'
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
              deployRepo  = 'snapshots'
              gradleTasks = 'artifactoryPublish'
              server      = Artifactory.newServer url: 'https://192.168.50.178:8888/artifactory', credentialsId: 'arti'
              rtGradle    = Artifactory.newGradleBuild()
              rtGradle.deployer repo: deployRepo, server: server
              rtGradle.deployer.deployMavenDescriptors = true
              rtGradle.resolver repo: 'public', server: server
              rtGradle.useWrapper = true
              rtGradle.usesPlugin = true

              rtGradle.deployer.addProperty("platform.template.id", "ru.alfalab.platform.template.api.simple:template-simple-api")
                               .addProperty("platform.template.version", "0.1.1")
        }
      }
    }
    stage('oas validate') {
      steps {
        script {
              sh './tools/install.sh'
              sh './tools/validate.sh'
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
     stage('publish') {
      steps {
        script {
          buildInfo = rtGradle.run switches: '--gradle-user-home $WORKSPACE/.gradle --stacktrace --info --console=plain',
          tasks: gradleTasks, buildInfo: buildInfo
        }
      }
      post {
        always {
          script {
            server.publishBuildInfo buildInfo
          }
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
