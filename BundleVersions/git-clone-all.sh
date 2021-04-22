#!/bin/bash
#set -e

base_url="ssh://git@stash.agilysys.local:7999"

echo "base_url: '${base_url}'"

repos=(
#!  cfg/baserolesetup
#  cfg/docker-build
#  cfg/inventory
#!  cfg/platformdeployment
#  cfg/pmsdeployment
#  cfg/rackspace-inventory
  #!cfg/securitymanagement
#!  pay/payeventinterface
#!  pay/payeventservice
#!  plat/appliance
#!  plat/authinterface
#!  plat/authservice
#!  plat/communicationinterface
#!  plat/communicationservice
#!  plat/contentinterface
#!  plat/contentservice
#!  plat/eventinterface
#!  plat/eventservice
#!  plat/examplecontentpackage
#!  plat/exampleservice
#!  plat/gatewayinterface
#!  plat/onpremisesgateway
#!  plat/parentpom
#!  plat/platformauth
#!  plat/platformdeploymentscripts
#!  plat/platform-internal-libraries
#!  plat/platforminternaltools
#!  plat/platform-libraries
#!  plat/platformqa
#!  plat/platformtools
#!  plat/platformui
#!  plat/qarestserviceframework
#!  plat/qarestservicetests
#!  plat/qatoolkittests
#!  plat/ruleengine
#!  plat/servicestubs
#!  plat/taxinterface
#!  plat/taxservice
#!  plat/ui-automation
#!  plat/userinterface
#!  plat/userservice
#!  plat/websockets
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
#!  pms/webservicearchetype
#!  pmsqa/rguest-qa
#!  pmsqa/xlocal
#  pmsqa/ui-automation
#  pmsqa/insertanator
#!  pmsqa/load-tests
)
for repo in ${repos[*]}; do
  if [ ! -d "$repo" ]; then
  	full_url="${base_url}/${repo}.git"
    echo "Cloning: ${full_url}"

	git clone "${full_url}" ${repo}
  fi
done
