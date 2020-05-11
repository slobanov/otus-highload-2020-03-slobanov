if [ -z "$1" ]
  then
    docker exec -it mysql mysql -u social-network-admin -ppassword social_network
  else
    docker exec -it mysql mysql -u social-network-admin -ppassword social_network -e "$1"
fi
