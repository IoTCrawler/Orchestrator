pipeline {
    agent {
	docker {
            image 'gitlab.iotcrawler.net:4567/core/maven-docker-compose:latest'
            args '-u root -v /var/jenkins_home/.m2:/root/.m2  -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker --network iotcrawler'
        }
    }
  stages{
      stage('Copying2'){   
        steps{
          sh 'rm -rf orchestrator'
          sh 'cp -R /mnt/share/orchestrator $HOME/orchestrator > /dev/null 2>&1'
          sh 'chmod -R 777 $HOME/orchestrator'
         }
      }
      stage('Build') {
          steps{
             sh 'mvn -version'
             sh 'docker-compose -version'
             sh 'cd $HOME/orchestrator && make install'
             //sh 'cd $HOME/orchestrator/core && mvn install -DskipTests=true'
             withCredentials([usernamePassword(credentialsId: 'b3a56de1-d06e-45c8-9ce4-5cc1ddb0cbb8', usernameVariable: 'CI_REGISTRY_USER', passwordVariable: 'CI_REGISTRY_PASSWORD')])     {
               sh 'docker login -u $CI_REGISTRY_USER -p $CI_REGISTRY_PASSWORD gitlab.iotcrawler.net:4567'
               sh 'cd $HOME/orchestrator/orchestrator && make build-image'
               sh 'cd $HOME/orchestrator/orchestrator && make push-image'                
            }                 
          }
      }
      stage('StartContainers') {
          steps{
             sh 'cd $HOME/orchestrator/orchestrator && make start-containers'  
             sh 'cd $HOME/orchestrator/orchestrator && docker-compose ps'
          }
      }  
//      stage('StartOrchestrator') {
//          steps{
//             sh 'cd /root/orchestrator/orchestrator && make package'
//             sh 'cd /root/orchestrator/orchestrator && make start &'  
//             sh 'ps'
//          }
//      }  
      stage('Tests') {
          steps{
            sh 'sleep 10'
            //sh 'cd /root/orchestrator/fiware/clients && make iot-broker-client-test'
               //sh 'cd /root/orchestrator/orchestrator && make orchestrator-test'
               sh 'cd $HOME/orchestrator/orchestrator && make rpc-client-test'
          }
       }
      //stage('Results') {
      //    steps{
      //      junit '**/target/surefire-reports/TEST-*.xml'
            //archiveArtifacts 'target/*.jar'
      //    }
      // }
  }
   post {
       always{
          sh 'cd $HOME/orchestrator/orchestrator && make stop-containers'   
       }
   }
}

