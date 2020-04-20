FROM mongo:3.6

RUN apt-get update \
  && apt-get install -y python3-pip python3-dev \
  && cd /usr/local/bin \
  && ln -s /usr/bin/python3 python \
  && pip3 install --upgrade pip

RUN pip install pymongo

COPY geojson-mongo-import.py /opt/geojson/
COPY world.geojson /opt/geojson/

WORKDIR /opt/geojson

RUN mongod --smallfiles --logpath=/var/log/mongodb/mongo.log # --quiet \
 python geojson-mongo-import.py -f world.geojson -d geodb -c countries