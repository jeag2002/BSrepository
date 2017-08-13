#include <stdio.h>
#include <windows.h>
#include <tchar.h>


BOOL GetLastWriteTime(HANDLE hFile, LPTSTR lpszString, DWORD dwSize)
{
    FILETIME ftCreate, ftAccess, ftWrite;
    SYSTEMTIME stUTC, stLocal;
    DWORD dwRet;

    // Retrieve the file times for the file.
    if (!GetFileTime(hFile, &ftCreate, &ftAccess, &ftWrite))
        return FALSE;

    // Convert the last-write time to local time.
    FileTimeToSystemTime(&ftWrite, &stUTC);
    SystemTimeToTzSpecificLocalTime(NULL, &stUTC, &stLocal);

    // Build a string showing the date and time.
    _tprintf(TEXT("%02d/%02d/%d  %02d:%02d"), stLocal.wDay, stLocal.wMonth, stLocal.wYear, stLocal.wHour, stLocal.wMinute, stLocal.wSecond);
    return TRUE;

}

int _tmain(int argc, TCHAR *argv[])
{
    HANDLE hFile;
    TCHAR szBuf[MAX_PATH];

    if( argc != 2 )
    {
        _tprintf("This sample takes a file name as a parameter\n");
        return 0;
    }
    hFile = CreateFile(argv[1], GENERIC_READ, FILE_SHARE_READ, NULL,
        OPEN_EXISTING, 0, NULL);

    if(hFile == INVALID_HANDLE_VALUE)
    {
        _tprintf("CreateFile failed with %d\n", GetLastError());
        return 0;
    }
    if(GetLastWriteTime( hFile, szBuf, MAX_PATH )){}
       //_tprintf(TEXT("Last write time is: %s\n"), szBuf);
}