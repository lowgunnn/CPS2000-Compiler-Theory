# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.16

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Lexer.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Lexer.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Lexer.dir/flags.make

CMakeFiles/Lexer.dir/Lexer.cpp.o: CMakeFiles/Lexer.dir/flags.make
CMakeFiles/Lexer.dir/Lexer.cpp.o: ../Lexer.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Lexer.dir/Lexer.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Lexer.dir/Lexer.cpp.o -c /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/Lexer.cpp

CMakeFiles/Lexer.dir/Lexer.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Lexer.dir/Lexer.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/Lexer.cpp > CMakeFiles/Lexer.dir/Lexer.cpp.i

CMakeFiles/Lexer.dir/Lexer.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Lexer.dir/Lexer.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/Lexer.cpp -o CMakeFiles/Lexer.dir/Lexer.cpp.s

# Object files for target Lexer
Lexer_OBJECTS = \
"CMakeFiles/Lexer.dir/Lexer.cpp.o"

# External object files for target Lexer
Lexer_EXTERNAL_OBJECTS =

Lexer: CMakeFiles/Lexer.dir/Lexer.cpp.o
Lexer: CMakeFiles/Lexer.dir/build.make
Lexer: CMakeFiles/Lexer.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable Lexer"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Lexer.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Lexer.dir/build: Lexer

.PHONY : CMakeFiles/Lexer.dir/build

CMakeFiles/Lexer.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Lexer.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Lexer.dir/clean

CMakeFiles/Lexer.dir/depend:
	cd /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug /mnt/c/Users/logan/Documents/GitHub/Compiler-Theory/cmake-build-debug/CMakeFiles/Lexer.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Lexer.dir/depend

