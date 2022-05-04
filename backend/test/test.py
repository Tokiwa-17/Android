
from flask import Blueprint, request, jsonify


test = Blueprint('test', __name__)

@test.route('/test', methods=['GET', 'POST'])
def test_url():
    return "test success!", 200