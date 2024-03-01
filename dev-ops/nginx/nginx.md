# 拷贝 docker 容器部署的 nginx 配置文件至本地
docker container cp Nginx:\etc\nginx\nginx.conf D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx
docker container cp Nginx:\etc\nginx\conf.d\default.conf D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\conf\conf.d
docker container cp Nginx:\usr\share\nginx\html\index.html D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\html

docker run --name Nginx -p 80:80 -v D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\logs:/var/log/nginx -v D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\html:/usr/share/nginx/html -v D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\conf\nginx.conf:/etc/nginx/nginx.conf -v D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\conf\conf.d:/etc/nginx/conf.d -v D:\Programming\Java\Projects\openai-chatgpt\dev-ops\nginx\ssl:/etc/nginx/ssl/ --privileged=true -d --restart=always nginx