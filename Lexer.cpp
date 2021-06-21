//
// Created by logan on 21/06/2021.
//

#include <fstream>
#include <iostream>
#include <string>
#include <sstream>
using namespace std;

using namespace std;

string getText(const string& filename);
string readFile1(string filename);

string getText(const string& filename){

    string text;
    string read;

    fstream file(filename);

    file.open(filename);

    while (file >> read) {
        // Output the text from the file
        text += read;
        cout << read << endl;
    }

    file.close();
     return text;
}

string readFile1( string filename )                                            // @JLBorges : direct from stream buffer
{
    ifstream file( filename );
    return { istreambuf_iterator<char>(file), {} };
}

int main() {

    cout << "Foxx ommok" << endl;
    string program = readFile1("example.txt");
    cout << program << endl;

}