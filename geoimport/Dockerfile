FROM python:3-slim

RUN pip install pymongo

COPY geojson-mongo-import.py /opt/geojson/
COPY world.geojson /opt/geojson/

WORKDIR /opt/geojson

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

CMD /wait && npm start
CMD python geojson-mongo-import.py -f world.geojson -d geodb -c countries -s mongodb