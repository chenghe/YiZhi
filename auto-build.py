# -*- coding: utf-8 -*-

import subprocess
import requests

PGYER_UPLOAD_URL   = "http://www.pgyer.com/apiv1/app/upload"
USER_KEY           = "8f9d205b1aaee0ff18353cc091c4908d"
API_KEY            = "ca1cbd6c6323f55e3d2364287dcd49e6"


cmd_git_log = "git log --pretty=format:'%s - %an'"
proc = subprocess.Popen(cmd_git_log,shell=True, stdout=subprocess.PIPE)
stdout, stderr = proc.communicate()    
message = stdout.decode()
index = message.find("\n")

commitMessage = message[0:index]
print(commitMessage)
filePath = "./app/build/outputs/apk/app-zhongmeban-release.apk"
files = {'file': open(filePath, 'rb')}
headers = {'enctype':'multipart/form-data'}
payload = {'uKey':USER_KEY, '_api_key':API_KEY, 'updateDescription':commitMessage}
r = requests.post(PGYER_UPLOAD_URL, data = payload ,files=files,headers=headers)
if r.status_code == requests.codes.ok:
    print ('is ok!')
else:
    print ('HTTPError,Code:'+r.status_code)
print(r.content)