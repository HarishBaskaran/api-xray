#!/bin/bash

base_url="ssh://git@stash.agilysys.local:7999"

echo "base_url: '${base_url}'"

branches=(
  VCTRS-73819
)

repos=(
  pms/accountinterface
  pms/accountservice
  pms/commentinterface
  pms/commentservice
  pms/contentpackage
  pms/datatools
  pms/housekeepingoptimization
  pms/integrationappliance
  pms/integrationinterface
  pms/integrationservice
  pms/mongoops
  pms/paymentinterface
  pms/paymentservice
  pms/pmscommon
  pms/pmsintegrationtest
  pms/profileinterface
  pms/profileservice
  pms/propertyinterface
  pms/propertyservice
  pms/rateinterface
  pms/rateservice
  pms/relayinterface
  pms/relayservice
  pms/reportsaggregator
  pms/reportinterface
  pms/reportservice
  pms/reservationinterface
  pms/reservationservice
 # pms/rgstay-versions
  pms/rguestpayshim
  pms/rgui
  pms/rtools
  pms/ruleengineservice
  pms/servicerequestinterface
  pms/servicerequestservice
  pms/swaggerservice
#!  pms/vagrant-pv
#!  pms/vagrant-stay
  pms/victorsrootpom
)
for branches in ${branches[*]}; do
	DIR = "${branches}"
	if  [ -d "${branches}" ]; then
		cd "${branches}"
	else
		mkdir "${branches}" && cd "${branches}"
	fi
	for repo in ${repos[*]}; do
		
		if [ ! -d "$repo" ]; then
			full_url="${base_url}/${repo}.git"
			echo "Pulling ${full_url}"
			
			git clone -b changeset/"${branches}" --single-branch "${full_url}"
		
		else 
			git pull origin changeset/"${branches}"
		fi
	done
	cd ..
done


