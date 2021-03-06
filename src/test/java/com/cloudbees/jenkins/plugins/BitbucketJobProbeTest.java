package com.cloudbees.jenkins.plugins;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.transport.URIish;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BitbucketJobProbeTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private BitbucketJobProbe probe;

    private BitbucketJobProbe payloadProcessor;

    @Before
    public void setUp() {
        payloadProcessor = new BitbucketJobProbe();
    }

    @Test
    public void testLooselyMatchSsh() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("ssh://git@git.private.private:7999/project/repo.git");
        payloadLink = new URIish("https://git.private.private/project/repo");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchHttps() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("https://git.private.private/scm/project/repo.git");
        payloadLink = new URIish("https://git.private.private/project/repo");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchHttpsWithScmPrefix() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("https://git.private.private/scmtool/scm/project/repo.git");
        payloadLink = new URIish("https://git.private.private/scmtool/project/repo");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchSsh_ContextPath() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("ssh://git.private.private/project/repo.git");
        payloadLink = new URIish("https://git.private.private/context/path/project/repo");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchHttps_ContextPath() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("https://git.private.private/context/path/scm/project/repo.git");
        payloadLink = new URIish("https://git.private.private/context/path/project/repo");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchSsh_Https() throws URISyntaxException {
        URIish cloneUrl;
        URIish payloadLink;

        cloneUrl = new URIish("ssh://git@git.example.com:2222/abcd/repository-name.git");
        payloadLink = new URIish("https://git.example.com/scm/abcd/repository-name.git");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }

    @Test
    public void testLooselyMatchSsh_No_Project() throws URISyntaxException {
        URIish cloneUrl = new URIish("ssh://git@git.example.com:2222/~doe/repository-name.git");
        URIish payloadLink = new URIish("ssh://git@git.example.com:2222/~doe/repository-name.git");
        Assert.assertTrue(payloadProcessor.looselyMatch(payloadLink, cloneUrl));
    }
}
