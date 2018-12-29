//version 0.0.1
def rtGradle
def gradleTasks = ''
def buildInfo = Artifactory.newBuildInfo()
def UUID_DIR = UUID.randomUUID().toString()

String deployRepo, repoSlug, projKey, branch, version

pipeline {
  agent {
    label {
      label 'mesos-j11-platform'
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
        script {
          server      = Artifactory.server 'local-arti'
          rtGradle    = Artifactory.newGradleBuild()

          switch(params.artifact_target_type) {
            case 'RELEASE':
              gradleTasks = 'final -Prelease.scope=patch'
              deployRepo  = 'libs-release-local'
            break
            case 'SNAPSHOT':
              deployRepo  = 'libs-snapshot-local'
              gradleTasks = 'snapshot -Prelease.scope=patch'
            break
            case 'BUILD':
              deployRepo  = ''
              gradleTasks = 'build'
            break
            default:
              markAsUnstable()
            break
          }

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
    stage('build') {
      steps {

          script {
             buildInfo = rtGradle.run switches: '--stacktrace --info --console=plain', tasks: gradleTasks, buildInfo: buildInfo
          }

      }

      post {
        always {
          script {
            //TODO avoid this
            if(!fileExists('build/project-version')) {
              markAsUnstable()
            }
            server.publishBuildInfo buildInfo
          }
        }
      }
    }
    stage('export info') {
      steps {
        appendArtifactInfo buildInfo
        script {
          version = readFile('build/project-version')
          buildInfo.modules.each {
            echo "module: "+it.dump()
            echo "artifacts: "+it.artifacts.dump()
            it.artifacts.each {
              if(it.type == 'app-jar') {
                def checksum = it.sha1
                echo "artifact sha1: $checksum"
                currentBuild.description = "artifact_app_sha1:$checksum"
                currentBuild.description += "\nversion:$version"
                currentBuild.description += "\nserviceName:$repoSlug"
              }
            }
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
