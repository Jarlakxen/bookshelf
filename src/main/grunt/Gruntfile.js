module.exports = function(grunt) {

	//Configuración del proyecto
	grunt.initConfig({


		clean: {
			options: {
				force: true,
			},
			css: {
				src: ['../webapp/css', '../webapp/js']
			}
		},

		less: {
			development: {
				options: {
					compress: true
				},
				files: {
					"../webapp/css/bookshelf.css": "../less/Bookshelf.less"
				}
			},
		},

		uglify: {
			externals: {
				files:{
					//'../webapp/external/angles-without-dependancies/libs/angles.min.js': ['../webapp/external/angles-without-dependancies/libs/angles.js']
				}
			},
			development: {
				files: [{
					expand: true,
					flatten: false,
					cwd: '../js/',
					src: '**/*.js',
					dest: '../webapp/js/',
					ext: '.js'
				}]
			}
		},

		cssmin: {
			development: {
				files: {
				}
			}
		},

		copy: {
			development: {
				files: [{
					expand: true,
					flatten: false,
					cwd: '../js/',
					src: '**/*.js',
					dest: '../webapp/js/',
					ext: '.js'
				}]
			}
		},

		watch: {
			css: {
				files: '../less/**/*.less',
				tasks: ['less'],
				options: {
					event: ['all']
				}
			},
			js: {
				files: '../js/**/*.js',
				tasks: ['copy:development'],
				options: {
					event: ['all']
				}
			},
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-cssmin');

	//Carga las tareas personalizadas
	grunt.loadTasks('tasks');

	// Tarea por default
	grunt.registerTask('default', ['clean', 'less:development', 'cssmin:development', 'uglify:externals', 'copy']);

	grunt.registerTask('release', ['clean', 'less:development', 'cssmin:development', 'uglify:externals', 'uglify:development']);
};

