#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'location_picker'
  s.version          = '0.0.1'
  s.summary          = 'A location picker plugin for Flutter.'
  s.description      = <<-DESC
A location picker plugin for Flutter.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'GoogleMaps'
  s.ios.deployment_target = '8.0'
  s.pod_target_xcconfig = {
       'FRAMEWORK_SEARCH_PATHS' => '$(inherited) $(PODS_ROOT)/**',
      'OTHER_LDFLAGS' => '$(inherited) -undefined dynamic_lookup'
     }
end

