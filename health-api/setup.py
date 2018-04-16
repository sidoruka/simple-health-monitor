from setuptools import setup, find_packages

setup(
    python_requires='>3.5.*',
    name='hm-api',
    version='0.1',
    py_modules=['hm_api'],
    packages=find_packages(),
    include_package_data=True,
    install_requires=[
        'click',
        'flask',
        'flask-restful',
        'pyfcm'
    ],
    entry_points='''
        [console_scripts]
        hm-api=hm_api:cli
    ''',
)
