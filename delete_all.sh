kubectl delete --all configmaps --namespace orchestrator-3-production
kubectl delete --all deployments --namespace orchestrator-3-production
kubectl delete --all services --namespace orchestrator-3-production
kubectl delete service ingress-nginx --namespace ingress-nginx
kubectl delete configmap tcp-services --namespace ingress-nginx

