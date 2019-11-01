pipeline {
    //def mvnHome
    agent {
        //docker { image 'tiangolo/docker-with-compose' }
        dockerfile true
    }
    tools { 
        maven 'M3'
        jdk 'jdk8' 
    }
  stages{
      stage('Build') {
          steps{
             //sh 'rm -rf orchestrator'
             //sh 'cp -R /mnt/share/orchestrator orchestrator'
             //sh 'echo "Sleeping 3s" && sleep 3'
             sh 'echo "PATH=$PATH"'
             sh 'echo "JAVA_HOME=$JAVA_HOME"'
             sh 'ls /usr/lib/jvm/'
             sh '$JAVA_HOME/bin/java -version'
             //sh 'export PATH="$PATH:$M2_HOME/bin" && mvn -version'
          }
      }
      //stage('Tests') {
          //steps{
              //sh 'export PATH =$PATH:$M2_HOME'
              //sh 'echo PATH = $PATH'
              //sh 'cd orchestrator/fiware/clients && mvn -Dtest=NgsiLDClientTest test'
             //sh 'docker-compose -version'
             //sh 'docker-compose -f orchestrator/fiware/clients/docker-compose.yml up -d'
             //sh 'orchestrator/fiware/clients'
             //sh 'cd orchestrator/fiware/clients docker-compose down'
             
          //}
      //}
      
      //stage('Results') {
      //    steps{
      //      junit '**/target/surefire-reports/TEST-*.xml'
       //     //archiveArtifacts 'target/*.jar'
       //   }
       //}
  }
}
