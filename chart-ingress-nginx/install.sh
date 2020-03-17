helm install ./chart-ingress-nginx --name ingress-nginx --namespace orchestrator-3-production
kubectl get service ingress-nginx --namespace production-orchestrator-3-production