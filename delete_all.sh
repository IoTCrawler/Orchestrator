#!/usr/bin/env bash
NS=$1
kubectl delete --all services --namespace $NS ||
kubectl delete --all deployments --namespace $NS ||
kubectl delete --all pods --namespace $NS ||
kubectl delete --all replicaset --namespace $NS ||
kubectl delete service ingress-nginx --namespace ingress-nginx ||
kubectl delete configmap tcp-services --namespace ingress-nginx ||
kubectl get all --namespace ingress-nginx