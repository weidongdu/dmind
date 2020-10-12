import json
from LAC import LAC
from flask import Flask, request, Response, jsonify

app = Flask(__name__)
#app.debug = True
lac = LAC(mode='seg')
lac.load_customization('dict.txt', sep=None)
@app.route('/seg',methods=['POST'])
def seg():
	if request.is_json:
		app.logger.debug('request json = %s', request.json)
		result = request.json['word']
		seg_result = lac.run(result)
		return Response(json.dumps(seg_result),  mimetype='application/json')
	else:
		return jsonify([])
if __name__ == '__main__':
    app.run(host='0.0.0.0',port=25000)
